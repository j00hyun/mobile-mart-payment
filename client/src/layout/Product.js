import React from 'react';
import BoardTable from "../table/table";
import Menu from "./Menu";

const Product = () => (
    <div className="footer">
        <div className="scard"/>
        <div className="tcard">과일 / 채소 / 양곡</div>
        <div className="dcard">축산 / 수산 / 건식품</div>
      <div className="card">
          <BoardTable/>
      </div>

    </div>
)

export default Product;