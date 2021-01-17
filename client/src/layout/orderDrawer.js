import React from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';

import {Link} from "react-router-dom"; 
import DialogContent from '@material-ui/core/DialogContent';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';
import './orderDrawer.css';
import Collapse from '@material-ui/core/Collapse';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';

const useStyles = makeStyles({
  list: {
    width: 450,
  },
  fullList: {
    width: 'auto',
  },
  nested: {
  },
});

const anchor = 'right'
export default function TemporaryDrawer() {
  const classes = useStyles();
  const [state, setState] = React.useState({
    top: false,
    left: false,
    bottom: false,
    right: false,
  });

  const [open, setOpen] = React.useState(true);

  const handleClick = () => {
    setOpen(!open);
  };

  const toggleDrawer = (anchor, open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    setState({ ...state, [anchor]: open });
  };

  const list = (anchor) => (
    <div
      className={clsx(classes.list, {
        [classes.fullList]: anchor === 'top' || anchor === 'bottom',
      })}
      role="presentation"

    >
<div>
<br/>
<span style={{
			fontSize: "1.7em",
				color: "#000000"
		}}>
&nbsp; 주문내역 &nbsp;&nbsp;
</span>
  <TextField id="standard-basic" label="주문날짜, 물품검색" />

 <IconButton type="submit" className={classes.iconButton} aria-label="search">
        <SearchIcon />
      </IconButton>

</div>
      <Divider />

      <List>
        {['ㆍ12월', 'ㆍ11월', 'ㆍ10월'].map((text, index) => (
          <ListItem button key={text} onClick={handleClick}>
            <ListItemText primary={text} />
{open ? <ExpandLess /> : <ExpandMore />}

          </ListItem>

        ))}
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          <ListItem button className={classes.nested}>
            <ListItemText primary="맛있는 리얼팜" />
          </ListItem>
          <ListItem button className={classes.nested}>
            <ListItemText primary="노브랜드 베이컨" />
          </ListItem>
          <ListItem button className={classes.nested}>

            <ListItemText primary="크림치즈케익" />
          </ListItem>
        </List>
      </Collapse>
      </List>




    </div>
  );

  return (
		  <div>
		  <React.Fragment key={anchor}>
		  <Link onClick = {toggleDrawer(anchor, true)} className={'tab_day on'}> 
			 주문 관리
		  </Link> 
          <Drawer anchor={anchor} open={state[anchor]} onClose={toggleDrawer(anchor, false)}>
            {list(anchor)}
          </Drawer>
        </React.Fragment>
    </div>
  );
}
