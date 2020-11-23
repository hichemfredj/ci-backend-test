package cal.internshipmanager.service;

import cal.internshipmanager.model.Contract;
import cal.internshipmanager.model.InternshipApplication;
import cal.internshipmanager.model.InternshipOffer;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.ContractRepository;
import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.response.ContractListResponse;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.response.UserListReponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ContractService {

    //
    // Dependencies
    //

    private final InternshipApplicationRepository internshipApplicationRepository;

    private final UserRepository userRepository;

    private final InternshipOfferRepository internshipOfferRepository;

    private final ContractRepository contractRepository;

    private final SettingsService settingsService;

    //
    // Constructors
    //

    @Autowired
    public ContractService(InternshipApplicationRepository internshipApplicationRepository,
                           UserRepository userRepository,
                           InternshipOfferRepository internshipOfferRepository,
                           ContractRepository contractRepository,
                           SettingsService settingsService) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.userRepository = userRepository;
        this.internshipOfferRepository = internshipOfferRepository;
        this.contractRepository = contractRepository;
        this.settingsService = settingsService;
    }

    //
    // Services
    //

    public ContractListResponse.Contract find(UUID uniqueId){
        return ContractListResponse.map(contractRepository.findById(uniqueId).get());
    }

    public void sign(UUID uniqueId) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        Contract contract = contractRepository.findById(uniqueId).get();

        if (contract.getStatus() != Contract.Status.COMPLETED && contract.getCurrentUserUniqueId().equals(userUniqueId)) {
            User user = userRepository.findById(userUniqueId).get();

            if (user.getSignature() != null) {

                switch (contract.getStatus()) {
                    case STUDENT:
                        contract.setStudentSignature(user.getSignature());
                        contract.setCurrentUserUniqueId(contract.getApplication().getOffer().getEmployer());
                        contract.setStatus(Contract.Status.EMPLOYER);
                        contract.setStudentSignedDate(new Date());
                        break;
                    case EMPLOYER:
                        contract.setEmployerSignature(user.getSignature());
                        contract.setCurrentUserUniqueId(contract.getAdministrator().getUniqueId());
                        contract.setStatus(Contract.Status.ADMINISTRATOR);
                        contract.setEmployerSignedDate(new Date());
                        break;
                    case ADMINISTRATOR:
                        contract.setAdministratorSignature(user.getSignature());
                        contract.setCurrentUserUniqueId(null);
                        contract.setStatus(Contract.Status.COMPLETED);
                        contract.setAdministratorSignedDate(new Date());
                        break;
                    default:
                        break;
                }
            }
        }

        contractRepository.save(contract);
    }

    public ContractListResponse awaitingSignature(UUID userUniqueId) {

        ContractListResponse response = new ContractListResponse();

        response.setContracts(contractRepository
                .findAllBySemesterAndCurrentUserUniqueId(settingsService.getSemester(), userUniqueId).stream()
                .map(ContractListResponse::map).collect(Collectors.toList()));

        return response;
    }

    // TODO add preauthorize for admin
    public void create(UUID applicationUniqueId) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        User administrator = userRepository.findById(userUniqueId).get();

        InternshipApplication application = internshipApplicationRepository.findById(applicationUniqueId).get();

        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setSemester(settingsService.getSemester());
        contract.setStatus(Contract.Status.STUDENT);
        contract.setApplication(application);
        contract.setAdministrator(administrator);
        contract.setCreationDate(new Date());
        contract.setCurrentUserUniqueId(application.getStudent().getUniqueId());

        contractRepository.save(contract);
    }

    // PDF Generation --------------------------------

    @SneakyThrows
    public DownloadFileResponse generate(UUID uniqueId) {

        Contract contract = contractRepository.findById(uniqueId).get();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, stream);

        document.open();

        write(contract, document);

        document.close();
        byte[] data = stream.toByteArray();
        DownloadFileResponse response = new DownloadFileResponse();

        response.setName("Contrat");
        response.setType("application/pdf");
        response.setResource(new ByteArrayResource(data));
        response.setLength(data.length);

        return response;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @SneakyThrows
    private void write(Contract contract, Document document) throws DocumentException {
        InternshipApplication application = contract.getApplication();

        User student = application.getStudent();
        User admin   = contract.getAdministrator();
        InternshipOffer offer = application.getOffer();

        User employer = userRepository.findById(offer.getEmployer()).orElse(null);



        Image image = Image.getInstance("src/main/resources/logo.png");

        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font bold = new Font(Font.FontFamily.HELVETICA,12, Font.BOLD);
        Font boldMid = new Font(Font.FontFamily.HELVETICA,13, Font.BOLD);
        Font boldFrontPage = new Font(Font.FontFamily.HELVETICA,20, Font.BOLD);

        Rectangle r = new Rectangle(36, 36, 559, 806);
        r.setBorder(Rectangle.BOX);
        r.setBorderWidth(2);
        r.setBorderColor(BaseColor.BLACK);
        Paragraph p = new Paragraph(new Phrase("Cegep Andre-Laurendeau",boldMid));
        p.setExtraParagraphSpace(5);
        Paragraph p2 = new Paragraph();

        p.setAlignment(Paragraph.ALIGN_MIDDLE);
        addEmptyLine(p2, 14);
        p2.add(setNewMIddleParagraph("Entente de stage cooperatif",boldFrontPage));
        addEmptyLine(p2, 1);
        p2.add(setNewMIddleParagraph("Technique informatique",boldMid));
        addEmptyLine(p2, 19);
        p2.add(setNewMIddleParagraph(""+application.getSemester(),null));
        p2.setAlignment(Paragraph.ALIGN_CENTER);

        document.setMargins(5,5,5,5);
        document.addTitle("Contrat");
        document.add(r);
        image.setScaleToFitHeight(true);
        image.setWidthPercentage(125);
        document.add(image);
        document.add(p2);


//-------------------------------------------------


        document.newPage();
        document.add(setNewEmptyParagraph(3));
        document.add(setNewMIddleParagraph("ENTENTE INTERVENUE ENTRE LES PARTIES SUIVANTES",bold));
        document.add(setNewEmptyParagraph(1));
        document.add(setNewMIddleParagraph("Dans le cadre de la formule Alternance travail-etudes du programme de",null));
        document.add(setNewMIddleParagraph(new Phrase("TECHNIQUE INFORMATIQUE",bold)+", les parties citees ci-dessous :",null));
        document.add(setNewEmptyParagraph(1));
        Rectangle r2 = new Rectangle(36, 586, 559, 686);
        r2.setBorder(Rectangle.BOX);
        r2.setBorderWidth(1);
        r2.setBorderColor(BaseColor.BLACK);
        document.add(r2);
        document.add(setNewEmptyParagraph(1));
        Phrase phrase = new Phrase();
        phrase.add(new Chunk("Le"));
        phrase.add(new Chunk("CÉGEP ANDRÉ-LAURENDEAU",bold));
        phrase.add(new Chunk(", corporation legalement constitue, situe au"));
        document.add(setNewMIddleParagraph(phrase));
        document.add(setNewMIddleParagraph("1111, rue Lapierre, LASALLE(Quebec), H8N 2J4,",null));
        document.add(setNewMIddleParagraph("ici represente par Monsieur Benoit Archambault",null));
        document.add(setNewMIddleParagraph("ci-apres designe <<Le College>>",null));
        document.add(setNewEmptyParagraph(2));
        document.add(setNewMIddleParagraph("et",bold));
        document.add(setNewEmptyParagraph(1));
        document.add(setNewMIddleParagraph(offer.getCompany()+ " ayant sa place d'affaire au:",null));
        document.add(setNewEmptyParagraph(1));
        PdfPTable table = new PdfPTable(1);
        PdfPTable table2 = new PdfPTable(2);
        table2.addCell(cellWithoutBorder("Addresse: "+ offer.getLocation()));
        table2.addCell(cellWithoutBorder("Ville: "));
        table2.addCell(cellWithoutBorder("Code Postal: "));
        table2.addCell(cellWithoutBorder("Telephone: "));
        PdfPCell cell = new PdfPCell(new Phrase("Nom de l'entreprise : "+offer.getCompany()));
        cell.setBackgroundColor(new BaseColor(232,232,232));
        table.addCell(cell);
        PdfPCell c2 = new PdfPCell(new Phrase("Personne contact en entreprise: " + employer.getFirstName() +" "+ employer.getLastName()));
        c2.setBorder(PdfPCell.NO_BORDER + PdfPCell.LEFT + PdfPCell.RIGHT);
        table.addCell(c2);
        table.addCell(table2);
        table.setPaddingTop(15);
        document.add(table);
        document.add(setNewEmptyParagraph(1));
        document.add(setNewMIddleParagraph("et",bold));
        document.add(setNewMIddleParagraph("L'etudiant, " + student.getFirstName()+ " "+ student.getLastName()+",",null));
        document.add(setNewMIddleParagraph("Conviennent des conditions de stage suivantes :",null));
        document.add(setNewEmptyParagraph(1));
        PdfPTable table3 = new PdfPTable(1);
        PdfPTable table4 = new PdfPTable(2);
        table4.addCell(cellWithoutBorder("Addresse : "));
        table4.addCell(cellWithoutBorder("Ville : "));
        table4.addCell(cellWithoutBorder("Code Postal : "));
        table4.addCell(cellWithoutBorder("Telephone : "));
        table3.addCell(setTitleCell("ENDROIT DU STAGE (si differente de l'addresse ci haut)"));
        PdfPCell c1 = new PdfPCell(new Phrase("Service ou departement : "));
        c1.setBorder(PdfPCell.NO_BORDER + PdfPCell.LEFT + PdfPCell.RIGHT);
        table3.addCell(c1);
        table3.addCell(table4);
        table3.addCell(setTitleCell("SUPERVISEUR DU STAGE"));
        PdfPTable table5 = new PdfPTable(2);
        table5.addCell(cellWithoutBorder("Nom: "+employer.getFirstName()+" "+employer.getLastName()));
        table5.addCell(cellWithoutBorder("Titre: "));
        table5.addCell(cellWithoutBorder("Telephone: "));
        table5.addCell(cellWithoutBorder("Poste : "));
        table5.addCell(cellWithoutBorder("Telecopieur : "));
        table5.addCell(cellWithoutBorder("Courriel : "+employer.getEmail()));
        table3.addCell(table5);
        table3.addCell(setTitleCell("DUREE DU STAGE : "));
        PdfPTable table6 = new PdfPTable(2);
        table6.addCell(cellWithoutBorder("Date de debut : "+ DateFor.format(offer.getStartDate())));
        table6.addCell(cellWithoutBorder("Date de fin : "+ DateFor.format(offer.getEndDate())));
        table6.addCell("Nombre total d'heures par semaine : ");
        table3.addCell(table6);
        table3.addCell(setTitleCell("HORAIRE DE TRAVAIL"));
        PdfPCell c4 = new PdfPCell(new Phrase("Horaire de travail : N/A"));
        c4.setBorder(PdfPCell.NO_BORDER + PdfPCell.LEFT + PdfPCell.RIGHT);
        table3.addCell(c4);
        PdfPCell c5 = new PdfPCell(new Phrase("Nombre total d'heures par semaine : "+offer.getHours()));
        c5.setBorder(PdfPCell.NO_BORDER + PdfPCell.LEFT + PdfPCell.RIGHT);
        table3.addCell(c5);
        table3.addCell(setTitleCell("SALAIRE"));
        PdfPCell c3 = new PdfPCell(new Phrase("Salaire horaire : "+ offer.getSalary()+"$"));
        c3.setBorder(PdfPCell.NO_BORDER + PdfPCell.LEFT + PdfPCell.RIGHT + PdfPCell.BOTTOM);
        table3.addCell(c3);
        table.setPaddingTop(15);
        document.add(table3);



        //------------------------------------------------------------



        document.setMargins(36,36,0,0);
        document.newPage();

        document.add(setNewEmptyParagraph(1));
        document.add(new Paragraph("Conformément aux objectifs visés pour ce stage, les tâches et les mandats suivants seront confiés à l’étudiant stagiaire:",new Font(Font.FontFamily.HELVETICA,9)));
        document.add(setNewEmptyParagraph(1));
        Paragraph p4 = new Paragraph(new Phrase("TACHES ET RESPONSABILITES DU STAGIAIRE",bold));
        PdfPTable table7 = new PdfPTable(1);
        PdfPTable table8 = new PdfPTable(1);
        table7.setWidthPercentage(100);
        table8.setWidthPercentage(100);
        PdfPCell cell6 = new PdfPCell();
        cell6.setBorder(PdfPCell.BOX);
        cell6.setBorderColor(BaseColor.BLACK);
        cell6.setBorderWidth(1);
        p4.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(p4);
        document.add(setNewEmptyParagraph(1));
        PdfPCell cell7;

        for (String job:offer.getJobScope()
             ) {
            cell7 = new PdfPCell();
            cell7.setBorder(0);
            cell7.addElement(new Phrase("-"+job));
            table7.addCell(cell7);
        }
        cell6.addElement(table7);
        table8.addCell(cell6);
        document.add(table8);
        document.add(setNewEmptyParagraph(1));
        document.add(setNewMIddleParagraph("RESPONSABILITES",bold));
        document.add(new Paragraph("Le Collège s’engage à :"));
        document.add(tinyTextParagraph("• fournir à l’entreprise tous les renseignements concernant les conditions spécifiques du programme d’études et du programme d’alternance" + "travail-études;"));
        document.add(tinyTextParagraph("• collaborer, au besoin, à la définition du plan de stage;"));
        document.add(tinyTextParagraph("• effectuer un suivi de l’étudiant stagiaire pendant la durée du stage;"));
        document.add(tinyTextParagraph("• fournir à l’entreprise les documents nécessaires à l’évaluation de l’étudiant stagiaire;"));
        document.add(tinyTextParagraph("• collaborer avec l’entreprise pour résoudre des problèmes qui pourraient survenir en cours de stage, le cas échéant;"));
        document.add(tinyTextParagraph("• conserver tous les dossiers de stage et les rapports des étudiants;"));
        document.add(tinyTextParagraph("• fournir à l’entreprise le formulaire d’attestation de participation à un stage de formation admissible après réception du formulaire « Déclaration" + "relative au crédit d’impôt remboursable pour les stages »."));
        document.add(new Paragraph("L’entreprise s’engage à :"));
        document.add(tinyTextParagraph("• embaucher l’étudiant stagiaire"+student.getFirstName()+" "+student.getLastName() +" aux conditions précisées dans la présente entente;"));
        document.add(tinyTextParagraph("• désigner un superviseur de stage qui assurera l’encadrement de l’étudiant stagiaire pour toute la durée du stage;"));
        document.add(tinyTextParagraph("• mettre en place des mesures d’accueil, d’intégration et d’encadrement de l’étudiant stagiaire;"));
        document.add(tinyTextParagraph("• procéder à l’évaluation de l’étudiant stagiaire."));
        document.add(tinyTextParagraph("• retourner le formulaire « Déclaration relative au crédit d’impôt remboursable pour les stages » dûment rempli."));
        document.add(new Paragraph("L'etudiant s'engage à :"));
        document.add(tinyTextParagraph("• assumer de façon responsable et sécuritaire, les tâches qui lui sont confiées;"));
        document.add(tinyTextParagraph("• respecter les politiques, règles et procédures de l’entreprise ainsi que l’horaire de travail au même titre qu’un employé;"));
        document.add(tinyTextParagraph("• respecter les dates de début et de fin de stage;"));
        document.add(tinyTextParagraph("• référer rapidement au responsable des stages au cégep toute situation problématique affectant le bon déroulement du stage;"));
        document.add(tinyTextParagraph("• rédiger un rapport de stage et le soumettre au responsable des stages au cégep."));
        document.add(setNewEmptyParagraph(1));
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.addCell(setTitleCell("SIGNATURES"));
        document.add(t);
        document.add(setNewMIddleParagraph("Les parties s'engagent a respecter cette entente de stage",null));
        document.add(new Paragraph(new Phrase("En foi de quoi les parties ont signe,",new Font(Font.FontFamily.HELVETICA,9,Font.BOLD))));
        PdfPCell cell8 = new PdfPCell();
        cell8.setBorder(0);
        PdfPTable t2 = new PdfPTable(1);
        PdfPTable t3 = new PdfPTable(new float[] { 3, 1 });
        PdfPCell tmpCell;
        t2.setWidthPercentage(100);
        t3.setWidthPercentage(100);
        if(contract.getStudentSignature() != null){
        image = Image.getInstance(contract.getStudentSignature().getData());
        image.scaleAbsolute(120,40);
        image.setAlignment(Element.ALIGN_MIDDLE);}
        tmpCell=assignBorder(new PdfPCell(new Phrase("",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD))),0,0,2);
        tmpCell.addElement(new Phrase("L’étudiant : ",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD)));
        if(contract.getStudentSignature() != null)
        tmpCell.addElement(image);
        tmpCell.setPadding(20);
        t3.addCell(tmpCell);
        if(contract.getStudentSignature() != null) {
            PdfPCell tmpCell2 = assignBorder(new PdfPCell(), 0, 0, 2);
            Paragraph p6 = new Paragraph(new Phrase("" + new Date()));
            p6.setAlignment(Paragraph.ALIGN_MIDDLE + Paragraph.ALIGN_BOTTOM);
            tmpCell2.addElement(p6);
            t3.addCell(tmpCell2);
        }
        else {
            t3.addCell(assignBorder(new PdfPCell(), 0, 0, 2));
        }
        PdfPCell tmpCell3=assignBorder(new PdfPCell(),0,0,0);
        Paragraph p7 = new Paragraph(new Phrase(student.getFirstName()+" "+student.getLastName()));
        p7.setAlignment(Paragraph.ALIGN_MIDDLE+Paragraph.ALIGN_TOP);
        tmpCell3.addElement(p7);
        t3.addCell(tmpCell3);
        t3.addCell(removeBorders(new Phrase("Date")));

        if(contract.getEmployerSignature() != null){
        image = Image.getInstance(contract.getEmployerSignature().getData());
        image.scaleAbsolute(120,40);
        image.setAlignment(Element.ALIGN_MIDDLE);}
        tmpCell=assignBorder(new PdfPCell(new Phrase("",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD))),0,0,2);
        tmpCell.addElement(new Phrase("Pour l’entreprise : ",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD)));
        if(contract.getEmployerSignature() != null)
        tmpCell.addElement(image);
        tmpCell.setPadding(20);
        t3.addCell(tmpCell);
        if(contract.getEmployerSignature() != null) {
            PdfPCell tmpCell2 = assignBorder(new PdfPCell(), 0, 0, 2);
            Paragraph p6 = new Paragraph(new Phrase("" + new Date()));
            p6.setAlignment(Paragraph.ALIGN_MIDDLE + Paragraph.ALIGN_BOTTOM);
            tmpCell2.addElement(p6);
            t3.addCell(tmpCell2);
        }
        else {
            t3.addCell(assignBorder(new PdfPCell(), 0, 0, 2));
        }
        tmpCell3=assignBorder(new PdfPCell(),0,0,0);
        p7 = new Paragraph(new Phrase(employer.getFirstName()+" "+employer.getLastName()));
        p7.setAlignment(Paragraph.ALIGN_MIDDLE+Paragraph.ALIGN_TOP);
        tmpCell3.addElement(p7);
        t3.addCell(tmpCell3);
        t3.addCell(removeBorders(new Phrase("Date")));

        if(contract.getAdministratorSignature() != null){
        image = Image.getInstance(contract.getAdministratorSignature().getData());
        image.scaleAbsolute(120,40);
        image.setAlignment(Element.ALIGN_MIDDLE);}
        tmpCell=assignBorder(new PdfPCell(new Phrase("",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD))),0,0,2);
        tmpCell.addElement(new Phrase("Pour le Collège : ",new Font(Font.FontFamily.HELVETICA,12,Font.BOLD)));
        if(contract.getAdministratorSignature() != null)
        tmpCell.addElement(image);
        tmpCell.setPadding(20);
        t3.addCell(tmpCell);
        if(contract.getAdministratorSignature() != null) {
            PdfPCell tmpCell2 = assignBorder(new PdfPCell(), 0, 0, 2);
            Paragraph p6 = new Paragraph(new Phrase("" + new Date()));
            p6.setAlignment(Paragraph.ALIGN_MIDDLE + Paragraph.ALIGN_BOTTOM);
            tmpCell2.addElement(p6);
            t3.addCell(tmpCell2);
        }
        else {
            t3.addCell(assignBorder(new PdfPCell(), 0, 0, 2));
        }
        tmpCell3=assignBorder(new PdfPCell(),0,0,0);
        p7 = new Paragraph(new Phrase(admin.getFirstName()+" "+admin.getLastName()));
        p7.setAlignment(Paragraph.ALIGN_MIDDLE+Paragraph.ALIGN_TOP);
        tmpCell3.addElement(p7);
        t3.addCell(tmpCell3);
        t3.addCell(removeBorders(new Phrase("Date")));
        cell8.addElement(t3);
        t2.addCell(cell8);
        document.add(t2);


    }

    private PdfPCell removeBorders(Phrase p){
        PdfPCell c = new PdfPCell(p);
        c.setBorder(0);
        return c;
    }

    private PdfPCell assignBorder(PdfPCell c,int border,int border2,int border3){
        c.setBorder(border + border2 + border3);
        return c;
    }

    private Paragraph tinyTextParagraph(String message){
        Paragraph p = new Paragraph(new Phrase(message,new Font(Font.FontFamily.TIMES_ROMAN,10)));

        return p;
    }


    private PdfPCell cellWithoutBorder(String message) {
        PdfPCell cell = new PdfPCell(new Phrase(message));
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell setTitleCell(String message) {
        PdfPCell cell = new PdfPCell(new Phrase(message,new Font(Font.FontFamily.HELVETICA,12,Font.BOLD)));
        cell.setBackgroundColor(new BaseColor(224,224,224));
        cell.setPadding(3);
        return cell;
    }

    private Paragraph setNewEmptyParagraph(int number) {
        Paragraph tmp = new Paragraph();
        for (int i = 0; i < number; i++) {
            tmp.add(new Paragraph(" "));
        }
        return tmp;
    }

    private  Paragraph setMiddle(Paragraph p){
        p.setAlignment(Paragraph.ALIGN_MIDDLE);
        return p;
    }

    private Paragraph setNewRightParagraph(String message) {
        Paragraph tmp = new Paragraph(message);
        tmp.setAlignment(Paragraph.ALIGN_RIGHT);
        return tmp;
    }


    private Paragraph setNewMIddleParagraph(Phrase message) {
        Paragraph tmp;
        tmp = new Paragraph(message);
        tmp.setAlignment(Paragraph.ALIGN_CENTER);
        return tmp;
    }

    private Paragraph setNewMIddleParagraph(String message,Font font) {
        Paragraph tmp;
        if(font==null)
            tmp = new Paragraph(message);
        else
            tmp = new Paragraph(new Phrase(message,font));
        tmp.setAlignment(Paragraph.ALIGN_CENTER);
        return tmp;
    }
}
