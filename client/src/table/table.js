import React from 'react';
import './table.css';
import {red} from "@material-ui/core/colors";

function BoardTable() {

    return (


        <table>

            <thead>
            <tr>
                <th scope="col">이미지</th>
                <th scope="col">품명</th>
                <th scope="col">남은 수량</th>
                <th scope="col">마지막 입고</th>
                <th scope="col">위치</th>
                <th scope="col">구매가</th>
                <th scope="col">판매가</th>


            </tr>
            </thead>
            <tbody>

            <tr style={{marginBottom: 20}}>
                <td><img
                    src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
                    width="70px"/></td>
                <td>치즈크림케익</td>
                <td className="yellow">32개</td>
                <td>2020.12.10</td>
                <td>A열 4번</td>
                <td className="red">20000원</td>
                <td className="blue">30000원</td>
            </tr>
            <tr style={{marginBottom: 20}}>
                <td><img
                    src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
                    width="70px"/></td>
                <td>치즈크림케익</td>
                <td className="yellow">32개</td>
                <td>2020.12.10</td>
                <td>A열 4번</td>
                <td className="red">20000원</td>
                <td className="blue">30000원</td>
            </tr>
            <tr style={{marginBottom: 20}}>
                <td><img
                    src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
                    width="70px"/></td>
                <td>치즈크림케익</td>
                <td className="yellow">32개</td>
                <td>2020.12.10</td>
                <td>A열 4번</td>
                <td className="red">20000원</td>
                <td className="blue">30000원</td>
            </tr>
            <tr style={{marginBottom: 20}}>
                <td><img
                    src="https://img.danawa.com/prod_img/500000/528/389/img/5389528_1.jpg?shrink=360:360&_v=20170809160044"
                    width="70px"/></td>
                <td>치즈크림케익</td>
                <td className="yellow">32개</td>
                <td>2020.12.10</td>
                <td>A열 4번</td>
                <td className="red">20000원</td>
                <td className="blue">30000원</td>
            </tr>


            </tbody>


        </table>

    )
}

export default BoardTable;
