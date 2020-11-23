import React, { useEffect, useState } from "react";
import { makeStyles } from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import PortfolioService from "../services/PortfolioService";
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import { saveAs } from 'file-saver';
import InternshipApplicationService from "../services/InternshipApplicationService";

export default function StudentDocumentsList (props) {

  const [rows, setRows] = useState([]);

  const fetchPortfolioDocuments = async () => {
    const response = await InternshipApplicationService.applicationDocuments(props.selectedValue);
    setRows(response.data.portfolioDocuments)
  }

  useEffect(() => {
    fetchPortfolioDocuments();
  }, [])

  const onDownloadClick = (document) =>{

    PortfolioService.download(document.uniqueId).then(response =>{
      saveAs(new Blob([response.data], { type: response.headers['content-type'] }), document.fileName);
    })
    
  }

  return (
    <div>
      <Container>
        <Box
          mb={2}
          paddingTop={2}
          textAlign="left"
        >
          <Typography component="h1" variant="h4" align="center">List Of Documents</Typography>
        </Box>
      </Container>

      <TableContainer>
        <Table size="small" aria-label="a dense table">
          <TableHead>
            <TableRow>
              <TableCell align="center"><strong>File Name</strong></TableCell>
              <TableCell align="center"><strong>Type</strong></TableCell>
              <TableCell align="center"><strong>Action</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row,index) => (
              <TableRow key={index}>
                <TableCell component="th" scope="row" align="center">{row.fileName}</TableCell>
                <TableCell component="th" scope="row" align="center">{row.type}</TableCell>
                <TableCell omponent="th" scope="row" >


                  <Box margin={1}>
                    <Button
                      variant="contained" color="secondary"
                      size="small"
                      startIcon={<CloudDownloadIcon />}
                      onClick={() => onDownloadClick(row)}
                    >
                      Télécharger
                    </Button>
                  </Box>

                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}