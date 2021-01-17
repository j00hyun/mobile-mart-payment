import {Link} from "react-router-dom";
import Order from "./orderDialog";
import OrderDraw from "./orderDrawer";
import React from 'react';
import ReactDOM from 'react-dom'; 

const Menu = (props) => (
    <ul className="menu">
        <li>
            <Link to="/" className={'tab_day logo'}> No Brand </Link>

        </li>
        <li>
            <Link to="/" className={'tab_day on'}> 재고 관리 </Link>
        </li>
        <li>
            <Link to="/search" className={'tab_day on'}> 매출 관리 </Link>
        </li>
        <li>
			<OrderDraw/>
        </li>

    </ul>

)

export default Menu;
