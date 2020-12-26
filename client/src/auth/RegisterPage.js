import { useForm } from 'react-hook-form';
import React, { useState } from 'react';
import { userQueryGQL, registerMutationGQL, meGQL, loginMutationGQL } from './mutation';
import { useMutation, useQuery } from '@apollo/react-hooks';
import { useAuthToken } from './authToken';
import { TextField } from '@material-ui/core';
import { removeDirectivesFromDocument } from '@apollo/client/utilities';

import './login.css';


const AuthenticationForm = () => {
    const [_, setAuthToken] = useAuthToken();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');
    const [idNum, setIdNum] = useState('');
    const [token, setToken] = useState('');
    const registermutation = registerMutationGQL;

    const [reg] = useMutation(registermutation, {
            onCompleted: (data) => {
                setAuthToken(data.token);
                setToken(data.token);
                localStorage.setItem('token', token);
                window.location.href = '/login';
            },
            refetchQueries: [{ query: meGQL }],
            variables: {
                username: username,
                idNum: idNum,
                password: password
            }

        }
    );


    return (


        <div className='login-wrap'>
            <div className='login-html'>
                <input id='tab-1' type='radio' name='tab' className='sign-in' checked /><label htmlFor='tab-1'
                                                                                               className='tab'>Sign
                Up</label>
                <input id='tab-2' type='radio' name='tab' className='sign-up' /><label htmlFor='tab-2'
                                                                                       className='tab'>Sign
                In</label>
                <div className='login-form'>
                    <div className='sign-in-htm'>
                        <div className='group'>
                            <label htmlFor='user' className='label'
                                   onChange={e => setLogin(e.target.value)}>이름</label>
                            <input type='text' placeholder='Username' onChange={e => setUsername(e.target.value)}
                                   className='input' />
                        </div>
                        <div className='group'>
                            <label htmlFor='pass' className='label'>이메일</label>
                            <input type='text' placeholder='IdNum' onChange={e => setIdNum(e.target.value)}
                                   className='input' />
                        </div>
                        <div className='group'>
                            <label htmlFor='pass' className='label'>패스워드</label>
                            <input type='text' placeholder='Password' onChange={e => setPassword(e.target.value)}
                                   className='input' />;
                        </div>

                        <div className='group'>
                            <TextField type='submit'
                                       onClick={reg}
                                       value='회원가입'
                                       className='button' />;
                        </div>
                        <div className='group'>
                            <a href='/login' className='button'>이미 회원이신가요?</a>
                        </div>
                    </div>
                    <div className='sign-up-htm'>

                    </div>
                </div>
            </div>
        </div>);
};

export default AuthenticationForm;