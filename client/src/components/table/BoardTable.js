import React, {useEffect, useState} from 'react';
import './table.css';
import {useQuery} from "@apollo/react-hooks";
import {MeQuery, SearchQuery, UserSearchQuery} from "../../util/graphql";

function BoardTable() {

    const [contents, setContents] = useState('');
    const [id, setId] = useState();
    const {data: da} = useQuery(MeQuery);

    useEffect(() => {
        if (da) {
            setId(da.me.idNum);
        }
    }, [da]);


    const {data} = useQuery(SearchQuery);

    useEffect(() => {
        if (data) {
            setContents(data.orders);
        }
    }, [data]);


    return (


        <table>
            <caption>주문자 현황</caption>
            <thead>
            <tr>
                <th scope="col">사용자 이름</th>
                <th scope="col">메뉴</th>
                <th scope="col">Hot/Ice</th>
                <th scope="col">주문일시</th>


            </tr>
            </thead>
            <tbody>
            {contents &&
            contents.map((content) => (
                <tr key={content._id} style={{marginBottom: 20}}>
                    <td>{content.username}</td>
                    <td>{content.menu}</td>
                    <td>{content.hi}</td>
                    <td>{content.createdAt}</td>


                </tr>

            ))}

            </tbody>
        </table>

    )
}

export default BoardTable;
