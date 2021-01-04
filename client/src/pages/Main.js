import React, {Component, useEffect, useState} from 'react';

import Header from "../layout/Header";
import Menu from "../layout/Menu";
import Product from "../layout/Product";
import {useQuery} from "@apollo/react-hooks";
import {FETCH_POSTS_QUERY} from "../util/graphql";
import HeaderMenu from "../layout/HeaderMenu";
import Searchbar from "../components/Searchbar";

function Main() {


    const {loading} = useQuery(FETCH_POSTS_QUERY);


    if (loading) return <div className="loader"></div>


    return (
        <div>
            <Menu/>
            <Header/>
            <Searchbar/>
            <HeaderMenu/>
            <Product/>
        </div>
    )

}

export default Main;