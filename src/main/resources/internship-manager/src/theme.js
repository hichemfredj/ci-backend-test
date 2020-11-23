import { red } from '@material-ui/core/colors';
import { createMuiTheme } from '@material-ui/core/styles';

// A custom theme for this app
const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#3d70b2',
    },
    secondary: {
      main: '#5596e6',
    },
    error: {
      main: '#ff1744',
    },
    background: {
      default: '#F0F1F2',
    },
  },
});

export default theme;