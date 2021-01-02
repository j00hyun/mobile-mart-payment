import React, {useEffect, useState} from 'react';
import Header from "../layout/Header";
import Menu from "../layout/Menu";
import Product from "../layout/Product";
import {useQuery} from "@apollo/react-hooks";
import {FETCH_POSTS_QUERY} from "../util/graphql";
import CRUDTable from "../components/CRUDTable";
import HeaderMenu from "../layout/HeaderMenu";

function Crud() {



    return (
        <div>
            <Menu/>
            <Header/>
            <HeaderMenu/>
            <Product/>
        </div>
    )

}

export default Crud;
