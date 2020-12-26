import React, {useEffect} from 'react';
import {createUseStyles, useTheme} from 'react-jss';
import {useHistory} from 'react-router-dom';
import SLUGS from '../../resources/links';
import {convertlinksToUrl} from '../../resources/utilities';
import LogoComponent from './LogoComponent';
import Menu from './MenuComponent';
import MenuItem from './MenuItemComponent';
import {useLogout} from '../../auth/Logout';
import {useAuthToken} from '../../auth/authToken';
import {useApolloClient, useMutation, useQuery} from '@apollo/react-hooks';
import {MeQuery} from "../../util/graphql";
import {LogoutMutation} from "../../util/mutation";

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
                window.location.href = '/login';


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

            <MenuItem
                id={SLUGS.dashboard}
                title='재고 관리 메인 페이지'
                onClick={() => onClick(SLUGS.dashboard)}
            />

            <MenuItem
                id={SLUGS.tickets}
                title='매출 관리 메인 페이지'
                onClick={() => onClick(SLUGS.tickets)}
            />

            <div className={classes.separator}></div>

            <MenuItem
                id={SLUGS.settings}
                title='카테고리 별 매출/ 수익 그래프'
                onClick={() => onClick(SLUGS.settings)}
            />

            <MenuItem id='login' title='로그인' onClick={() => onClick(SLUGS.login)}/>
            {/*<MenuItem id='logout' title='로그아웃' onClick={logoutMutation}/>*/}

        </Menu>
    );
}

export default SidebarComponent;




