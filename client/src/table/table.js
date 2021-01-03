import React from 'react';
import './table.css';
import {red} from "@material-ui/core/colors";

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


	return (
		<div>
		<Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
		<DialogTitle id="customized-dialog-title" onClose={handleClose}>

		</DialogTitle>
		<DialogContent >
		<div class="container-fluid">
		<div class="row">
		<div class="col-md-12">
		<div  >
		<div style={{
			float:"left"
		}}>
		<label for="field_rest">
		<span>남은 수량  </span>
		</label>
		<input type="text" class="form-control" id="field_rest" />
		<img alt="nobrand_sample" src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044" />
		</div>
		<div style={{
			float:"left"
		}}>
		<form role="form">
		<div class="form-group">
		<p> 치즈크림케익 </p>
		<h5>.</h5>
		<label for="field_rest">
		<span>남은 수량 </span>
		</label>
		<TextField id="filled-basic"  variant="filled" size="small"/>
		<label for="field_auto">
		<span>자동 주문 </span>
		</label>
		<FormControl className={classes.formControl}>
		<Select
		labelId="demo-simple-select-label"
		id="demo-simple-select"
		value={age}
		onChange={handleChange}
		>
		<MenuItem value={1}>1 개</MenuItem>
		<MenuItem value={2}>2 개</MenuItem>
		<MenuItem value={3}>3 개</MenuItem>
		</Select>
		</FormControl>
		</div>
		<div class="form-group">

		<label for="field_last">
		<span>마지막 입고 </span>
		</label>
		<TextField id="field_last"  variant="filled" size="small"/>
		</div>
		<div class="form-group">

		<label for="field_location">
		<span>상품 위치  </span>  
		</label>
		<TextField id="field_location"  variant="filled" size="small" />
		</div>
		<div class="form-group">

		<label for="field_sell">
		<span>판매가    </span>  
		</label>
		<TextField id="field_sell"  variant="filled" size='small'/>
		</div>
		<div class="form-group">

		<label for="field_buy">
		<span>구매가    </span>  
		</label>
		<TextField id="field_sell"  variant="filled" size='medium'/>
		</div>
		<Button     style={{
			borderRadius: 5,
				backgroundColor: "#FFCC33",
				padding: "9px 18px",
				fontSize: "14px"
		}}>
		완료
		</Button>
		</form>
		<table >

		<thead>
		</thead>
		<tbody>

		<tr class="table_soo">
		<td rowspan="7"> <img alt="nobrand_sample" src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044" width="250" height="250"/></td>
		<td> <p> 치즈크림케익 </p></td>
		</tr>

		<tr bgcolor ="#FFFFFF">

		<td>4 </td>
		</tr>

		<tr>

		<td>							<div><label for="field_last">
		<span>마지막 입고 </span>
		</label>
		<TextField id="field_last"  variant="filled" size="small"/></div>
		</td>
		</tr>

		<tr>

		<td> 							<label for="field_location">
		<span>상품 위치  </span>  
		</label>
		<input type="text" id="field_location"  />
		</td>
		</tr>

		<tr>

		<td> 1</td>
		</tr>
		<tr>

		<td> 3</td>
		</tr>

		<tr>

		<td align="right"> <Button     style={{
			borderRadius: 5,
				backgroundColor: "#FFCC33",
				padding: "9px 18px",
				fontSize: "14px"
		}}>
		완료
		</Button></td>
		</tr>

		</tbody>
		</table>

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


function BoardTable() {
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


		<table border bgcolor="#FFFFFF">

		<thead>
		<tr>
		<th scope="col">이미지</th>
		<th scope="col">품명</th>
		<th scope="col">남은 수량</th>
		<th scope="col">마지막 입고</th>
		<th scope="col">위치</th>
		<th scope="col">구매가</th>
		<th scope="col">판매가</th>


		</tr>
		</thead>
		<tbody>
		<SimpleDialog selectedValue={selectedValue} open={open} onClose={handleClose}/>

		<tr style={{marginBottom: 20}}>
		<td><button onClick={handleClickOpen}><img
		src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
		width="70px"/></button></td>
		<td>치즈크림케익</td>
		<td className="yellow">32개</td>
		<td>2020.12.10</td>
		<td>A열 4번</td>
		<td className="red">20000원</td>
		<td className="blue">30000원</td>
		</tr>
		<tr style={{marginBottom: 20}}>
		<td><img
		src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
		width="70px"/></td>
		<td>치즈크림케익</td>
		<td className="yellow">32개</td>
		<td>2020.12.10</td>
		<td>A열 4번</td>
		<td className="red">20000원</td>
		<td className="blue">30000원</td>
		</tr>
		<tr style={{marginBottom: 20}}>
		<td><img
		src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
		width="70px"/></td>
		<td>치즈크림케익</td>
		<td className="yellow">32개</td>
		<td>2020.12.10</td>
		<td>A열 5번</td>
		<td className="red">20000원</td>
		<td className="blue">30000원</td>
		</tr>
		<tr style={{marginBottom: 20}}>
		<td><img
		src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
		width="70px"/></td>
		<td>치즈크림케익</td>
		<td className="yellow">32개</td>
		<td>2020.12.10</td>
		<td>A열 4번</td>
		<td className="red">20000원</td>
		<td className="blue">30000원</td>
		</tr>


		</tbody>


		</table>

	)
}

export default BoardTable;
