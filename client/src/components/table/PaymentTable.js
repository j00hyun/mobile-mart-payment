import React, {useEffect, useState} from 'react';
import './table.css';
import {useQuery} from "@apollo/react-hooks";
import {CupQuery} from "../../graphql/query";


function BoardTable() {

    const [hame, setHame] = useState();
    const [iame, setIame] = useState();

    const [hcafe, setHcafe] = useState();
    const [icafe, setIcafe] = useState();

    const [itea, setItea] = useState();

    const [hvan, setHvan] = useState();
    const [ivan, setIvan] = useState();

    const {data: da} = useQuery(CupQuery);

    console.log(da);
    useEffect(() => {
        if (da) {
            setHame(da.coffeeAmount[0]);
            setIame(da.coffeeAmount[1]);
            setHcafe(da.coffeeAmount[2]);
            setIcafe(da.coffeeAmount[3]);
            setItea(da.coffeeAmount[5]);
            setHvan(da.coffeeAmount[6]);
            setIvan(da.coffeeAmount[7]);


        }
    }, [da]);


    console.log(hame)

    return (


        <table>
            <caption>주문자 현황</caption>
            <thead>
            <tr>
                <th scope="col">메뉴</th>
                <th scope="col">Hot/Ice</th>
                <th scope="col">누적 잔 수</th>


            </tr>
            </thead>
            <tbody>
            <tr>
                <th scope="col">아메리카노</th>
                <th scope="col">Hot</th>
                <th scope="col">{hame}</th>
            </tr>
            <tr>
                <th scope="col">아메리카노</th>
                <th scope="col">Ice</th>
                <th scope="col">{iame}</th>
            </tr>


            <tr>
                <th scope="col">카페모카</th>
                <th scope="col">Hot</th>
                <th scope="col">{hcafe}</th>
            </tr>
            <tr>
                <th scope="col">카페모카</th>
                <th scope="col">Ice</th>
                <th scope="col">{icafe}</th>
            </tr>


            <tr>
                <th scope="col">아이스티</th>
                <th scope="col">Ice</th>
                <th scope="col">{itea}</th>
            </tr>


            <tr>
                <th scope="col">바닐라라떼</th>
                <th scope="col">Hot</th>
                <th scope="col">{hvan}</th>
            </tr>
            <tr>
                <th scope="col">바닐라라떼</th>
                <th scope="col">Ice</th>
                <th scope="col">{ivan}</th>
            </tr>

            </tbody>
        </table>

    )
}

export default BoardTable;
