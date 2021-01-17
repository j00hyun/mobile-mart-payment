import React, {useEffect, useState} from 'react';
import Header from "../layout/Header";
import Menu from "../layout/Menu";
import Product from "../layout/Product";
import HeaderMenu from "../layout/HeaderMenu";
import DashboardComponent from "../route/dashboard";

function Crud() {


    return (
        <div>
            <Menu/>
            <div className="header">
                매출 관리
            </div>
           <DashboardComponent/>
        </div>
    )

}

export default Crud;
