import React, {useEffect, useState} from 'react';
import './table.css';
import {useQuery} from "@apollo/react-hooks";
import {MeQuery, UserSearchQuery} from "../../graphql/query";
import DeleteButton from "../button/DeleteButton";

function BoardTable() {

    const [contents, setContents] = useState('');
    const [name, setName] = useState();

    const {data: da} = useQuery(MeQuery);

    useEffect(() => {
        if (da) {
            setName(da.me.username);
        }
    }, [da]);


    const {data} = useQuery(UserSearchQuery, {
        variables: {
            search: name

        }
    });

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
                <th scope="col">주문취소</th>


            </tr>
            </thead>
            <tbody>
            {contents &&
            contents.map((content) => (
                <tr key={content._id} style={{marginBottom: 20}}>
                    <td>{content.username}</td>
                    <td>{content.menu}</td>
                    <td>{content.hi}</td>
                    <td><DeleteButton post_id={content._id}/></td>


                </tr>

            ))}

            </tbody>
        </table>

    )
}

export default BoardTable;
