import React from 'react';
import Header from "../layout/Header";
import Menu from "../layout/Menu";
import Product from "../layout/Product";
import HeaderMenu from "../layout/HeaderMenu";
import Searchbar from "../components/Searchbar";


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
