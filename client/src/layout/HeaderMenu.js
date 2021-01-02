import React from 'react';
import {Link} from "react-router-dom";

const Menu = (props) => (
    <ul className="hmenu">
        <li>
            <Link to="/" className={'tab_day on'}> 신선 식품 </Link>
        </li>
        <li>
            <Link to="/search" className={'tab_day on'}> 가공 식품 </Link>
        </li>
        <li>
            <Link to="/crud" className={'tab_day on'}>생활용품</Link>
        </li>
        <li>
            <Link to="/search" className={'tab_day on'}>가전/인테리어</Link>
        </li>
        <li>
            <Link to="/crud" className={'tab_day on'}>잡화</Link>
        </li>
    </ul>
)

export default Menu;