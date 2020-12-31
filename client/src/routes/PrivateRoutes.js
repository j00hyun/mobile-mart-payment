import React, {Suspense, lazy} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import LINKS from '../resources/links';
import LoadingComponent from '../components/loading';
import PaymentboardComponent from "./paymentboard";
import UserboardComponent from "./userboard";

const OrderboardComponent = lazy(() => import('./orderboard/OrderBoardComponent'));

function PrivateRoutes() {
    return (
        <Suspense fallback={<LoadingComponent loading/>}>

            <Switch>
                <Route exact path={LINKS.orderboard} component={OrderboardComponent}/>
                <Route exact path={LINKS.tickets} component={PaymentboardComponent}/>
                <Route exact path={LINKS.settings} component={UserboardComponent}/>

                <Redirect to={LINKS.orderboard}/>
            </Switch>
        </Suspense>
    );
}

export default PrivateRoutes;
