import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import Switch from '@material-ui/core/Switch';
import {Link} from "react-router-dom";
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

const useStyles = makeStyles((theme) => ({
 form: {
     display: 'flex',
     flexDirection: 'column',
     margin: 'auto',
     width: 'fit-condent',
   },
 formControl: {
     marginTop: theme.spacing(2),
     minWidth: 500,
   },
 formControlLabel: {
     marginTop: theme.spacing(1),
   },
}));

export default function MaxWidthDialog() {
const classes = useStyles();
const [open, setOpen] = React.useState(false);

const handleClickOpen = () => {
setOpen(true);
};

const handleClose = () => {
setOpen(false);
};


const customContentStyle = {
width: "800px",
	   float: "right",
		   margin:" 0px 500px 0px 0px"
				
};

return (
		<React.Fragment>
<Link onClick = {handleClickOpen} className={'tab_day on'}>
주문내역
</Link>
<Dialog
fullWidth={true}
maxWidth={'sm'}
open={open}
margin={ '0px 500px 0px 0px'}
float={'right'}
onClose={handleClose}
aria-labelledby="max-width-dialog-title"
contentStyle={customContentStyle}
>
<DialogTitle>
<div>
주문내역&nbsp;&nbsp;&nbsp;&nbsp;
  <TextField id="standard-basic" label="주문날짜, 물품검색" />
 <IconButton type="submit" className={classes.iconButton} aria-label="search">
        <SearchIcon />
      </IconButton>
	  </div>
</DialogTitle>

<DialogContent>
<h4>ㆍ12월</h4>
<br/>
<h4>ㆍ11월</h4>

</DialogContent>
<DialogActions>
<Button onClick={handleClose} color="primary">
Close
</Button>
</DialogActions>
</Dialog>
</React.Fragment>
);
}
