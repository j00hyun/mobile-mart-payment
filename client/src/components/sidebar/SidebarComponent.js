import React, {useEffect} from 'react';
import {createUseStyles, useTheme} from 'react-jss';
import {useHistory} from 'react-router-dom';
import SLUGS from '../../resources/links';
import {convertlinksToUrl} from '../../resources/utilities';
import LogoComponent from './LogoComponent';
import Menu from './MenuComponent';
import MenuItem from './MenuItemComponent';
import {useAuthToken} from '../../routes/auth/authToken';
import {useApolloClient, useMutation, useQuery} from '@apollo/react-hooks';
import {MeQuery} from "../../graphql/query";
import {Row} from "simple-flexbox";
import {LogoutMutation} from "../../graphql/mutation";

const useStyles = createUseStyles({
    separator: {
        borderTop: ({theme}) => `1px solid ${theme.color.lightGrayishBlue}`,
        marginTop: 16,
        marginBottom: 16,
        opacity: 0.06
    }
});

function SidebarComponent() {
    const [_, removeAuthToken] = useAuthToken();

    const {push} = useHistory();
    const theme = useTheme();
    const classes = useStyles({theme});
    const isMobile = window.innerWidth <= 1080;


    const {data} = useQuery(MeQuery);



    const [logoutMutation, {loading}] = useMutation(LogoutMutation, {
            refetchQueries: [{query: MeQuery}],
            onCompleted: () => {
                removeAuthToken();
                localStorage.clear();
                window.location.href = '/';



            }
        }
    )

    function onClick(slug, parameters = {}) {
        push(convertlinksToUrl(slug, parameters));
    }

    return (
        <Menu isMobile={isMobile}>
            <div style={{paddingTop: 30, paddingBottom: 30}}>
                <LogoComponent/>
            </div>

            {data&&<MenuItem
                id={SLUGS.orderboard}
                title='주문자 페이지'
                onClick={() => onClick(SLUGS.orderboard)}
            />}

            {data&&<MenuItem
                id={SLUGS.tickets}
                title='결제자 페이지'
                onClick={() => onClick(SLUGS.tickets)}
            />}

            <div className={classes.separator}></div>
            {data &&
            <MenuItem
                id={SLUGS.settings}
                title='유저 페이지'
                onClick={() => onClick(SLUGS.settings)}
            />}

            {!data && <MenuItem id='login' title='로그인'   onClick={() => onClick(SLUGS.login)}/>}
            {data&&<MenuItem id='logout' title='로그아웃' onClick={logoutMutation}/>}

        </Menu>
    );
}

export default SidebarComponent;




