import React from 'react';
import {Link} from "react-router-dom";

const Menu = (props) => (
    <ul className="menu">
        <li>
            <Link to="/" className={'tab_day logo'}> No Brand </Link>

        </li>
        <li>
            <Link to="/" className={'tab_day on'}> 재고 관리 </Link>
        </li>
        <li>
            <Link to="/crud" className={'tab_day on'}> 매출 관리 </Link>
        </li>
        <li>
            <Link to="/crud" className={'tab_day on'}>주문 내역</Link>
        </li>

    </ul>

)

export default Menu;