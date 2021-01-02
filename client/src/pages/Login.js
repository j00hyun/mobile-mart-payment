//LoginPage.js


import React, {useState} from 'react';

import './login.css';


const AuthenticationForm = () => {

    return (


        <div className="login-page">
            <div className="form">
                <form>
                    <p className="logo">No Brand</p>
                    <p className="message">브랜드가 아니다. 소비자다</p>

                    <input type="text" placeholder="아이디"/>
                    <input type="text" placeholder="비밀번호 6자~20자 이하"/>
                    <button type='submit'
                            value='Login'
                    >로그인
                    </button>

                </form>
            </div>
        </div>
    );
};

export default AuthenticationForm;
