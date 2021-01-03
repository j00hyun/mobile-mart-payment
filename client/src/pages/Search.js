import React, {Component, useState, useEffect} from 'react';
import Header from "../layout/Header";
import Menu from "../layout/Menu";
import Product from "../layout/Product";
import SearchBar from "../components/SearchBar";
import HeaderMenu from "../layout/HeaderMenu";


function Search() {


    return (
        <div>
            <Menu/>
            <Header/>
            <HeaderMenu/>
            <Product/>
        </div>
    )

}

export default Search;
