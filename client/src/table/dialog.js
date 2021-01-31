import React from 'react';
import './dialog.css';
import DialogDetail from './dialogDetail'

import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import PersonIcon from '@material-ui/icons/Person';
import AddIcon from '@material-ui/icons/Add';
import Typography from '@material-ui/core/Typography';
import { blue } from '@material-ui/core/colors';

import { withStyles } from '@material-ui/core/styles';

import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';

import TextField from '@material-ui/core/TextField';

import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
const emails = ['username@gmail.com', 'user02@gmail.com'];

const useStyles = makeStyles({
	avatar: {
		backgroundColor: blue[100],
		color: blue[600],
	},
});

function SimpleDialog(props) {
	const classes = useStyles();
	const { onClose, selectedValue, open } = props;

	const [age, setAge] = React.useState('');

	const handleChange = (event) => {
		setAge(event.target.value);
	};
	const handleClose = () => {
		onClose(selectedValue);
	};

	const handleListItemClick = (value) => {
		onClose(value);
	};

	const styles = (theme) => ({
		root: {
			margin: 0,
			padding: theme.spacing(2),
		},
		closeButton: {
			position: 'absolute',
			right: theme.spacing(1),
			top: theme.spacing(1),
			color: theme.palette.grey[500],
		},
	});

	const DialogTitle = withStyles(styles)((props) => {
		const { children, classes, onClose, ...other } = props;
		return (
			<MuiDialogTitle disableTypography className={classes.root} {...other}>
			<Typography variant="h6">{children}</Typography>
			{onClose ? (
				<IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
				<CloseIcon />
				</IconButton>
			) : null}
			</MuiDialogTitle>
		);
	});

	const DialogContent = withStyles((theme) => ({
		root: {
			padding: theme.spacing(2),
		},
	}))(MuiDialogContent);

	const DialogActions = withStyles((theme) => ({
		root: {
			margin: 0,
			padding: theme.spacing(1),
		},
	}))(MuiDialogActions);

	const customContentStyle = {
		  width: "600px",
		  maxWidth: 'none',
	};
	return (
		<div>
			<Dialog
				onClose={handleClose} 
				aria-labelledby="customized-dialog-title" 
				open={open} 
				contentStyle={customContentStyle}
			>
			<DialogTitle id="customized-dialog-title" onClose={handleClose}>

			</DialogTitle>
			<DialogContent >
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12">
					<div style={{
						display:"flex",
						float:"left"
						}}>
							<div style={{
								display:"flex",
								float:"left"
								}}>

								<img alt="nobrand_sample" src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044" width="280" height="280"/>
							</div>
							<div style={{
								display:"flex",
								float:"left",
								width:"300px"
								}}>
								<form role="form">
								<div class="form-group">
									<p> 치즈크림케익 </p>
		<br/><br/>
		<label for="field_rest">
		<span>남은수량 &nbsp;&nbsp;</span>
		</label>
		<span class="span_1"> 34개 </span>
		<label for="field_auto">
		<span>&nbsp;자동 주문 &nbsp;</span>
		</label>
		<span class="span_1"> 3개이하</span>
		</div>
		<br/>
		<div class="form-group">

		<label for="field_last">
		<span>마지막입고 </span>
		</label>
		<span class="span_1">2021.1.31 </span>
		</div>
		<br/>
		<div class="form-group">

		<label for="field_location">
		<span>상품 위치 &nbsp; </span>  
		</label>
		<span class="span_1">A열 14번 </span>
		</div>
		<br/>
		<div class="form-group">

		<label for="field_sell">
		<span>판매가   &nbsp;&nbsp;&nbsp; &nbsp;</span>  
		</label>
		<span class="span_1">20000원 </span>
		</div>
		<br/>
		<div class="form-group">

		<label for="field_buy">
		<span>구매가  &nbsp;&nbsp;&nbsp;&nbsp;  </span>  
		</label>
		<span class="span_1">30000원 </span>
		</div>
		<DialogDetail/>

		</form>

		</div>
		</div>
		</div>
		</div>
		</div>
		</DialogContent>
		<DialogActions>
		</DialogActions>
		</Dialog>
		</div>
	);
}

SimpleDialog.propTypes = {
	onClose: PropTypes.func.isRequired,
	open: PropTypes.bool.isRequired,
	selectedValue: PropTypes.string.isRequired,
};
function Dialog1() {
	const [open, setOpen] = React.useState(false);
	const [selectedValue, setSelectedValue] = React.useState(emails[1]);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = (value) => {
		setOpen(false);
		setSelectedValue(value);
	};
	return (
<div>
		<SimpleDialog selectedValue={selectedValue} open={open} onClose={handleClose}/>

		<button onClick={handleClickOpen}><img
		src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
		width="70px"/></button>
</div>
	)
}

export default Dialog1
