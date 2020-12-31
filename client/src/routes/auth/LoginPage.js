import React, {useState} from 'react';
import {meGQL, loginMutationGQL} from './mutation';
import {useMutation} from '@apollo/react-hooks';
import {useAuthToken} from './authToken';
import './login.css';


const AuthenticationForm = () => {
    const [_, setAuthToken] = useAuthToken();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const loginmutation = loginMutationGQL;


    const [log, {loading}] = useMutation(loginmutation, {
            refetchQueries: [{query: meGQL}],
            onCompleted: (data) => {
                setAuthToken(data.login.token);
                localStorage.setItem('myData', data.login.token);
                window.location.href = '/order';


            },
            onError: () => {
                alert("로그인에 실패했습니다.")
            },
            variables: {
                login: login,
                password: password
            }
        }
    );


    return (


        <div className="login-page">
            <div className="form">
                <form className="login-form">
                    <p className="message">브랜드가 아니다. 소비자다</p>

                    <input type="text" placeholder="아이디" onChange={e => setLogin(e.target.value)}/>
                    <input type="text" placeholder="비밀번호 6자~20자 이하" onChange={e => setPassword(e.target.value)}/>
                    <button type='submit'
                            onClick={log}
                            unable={loading}
                            value='Login'
                    >로그인</button>

                </form>
            </div>
        </div>
    );
};

export default AuthenticationForm;