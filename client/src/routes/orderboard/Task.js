import React, {useEffect, useState} from 'react';
import {Row} from 'simple-flexbox';
import {createUseStyles, useTheme} from 'react-jss';
import {IconCheckboxOn, IconCheckboxOff} from '../../components/icons';
import CardComponent from '../../components/cards/CardComponent';
import {useMutation, useQuery} from "@apollo/react-hooks";
import {TaskQuery} from "../../graphql/query";
import {TaskCreateMutation} from "../../graphql/mutation";
import TaskDeleteButton from "../../components/button/TaskDeleteButton";

const useStyles = createUseStyles((theme) => ({
    addButton: {
        backgroundColor: theme.color.lightGrayishBlue,
        color: theme.color.grayishBlue2,
        fontSize: '20px !important',
        padding: '7px !important',
        width: "fit-content"
    },
    itemTitle: {
        ...theme.typography.itemTitle,
        color: theme.color.veryDarkGrayishBlue
    },
    itemValue: {
        color: theme.color.grayishBlue2
    },
    greyTitle: {
        color: theme.color.grayishBlue3
    },
    tagStyles: {
        borderRadius: 5,
        cursor: 'pointer',
        fontWeight: 'bold',
        fontSize: 11,
        letterSpacing: '0.5px',
        lineHeight: '14px',
        padding: '5px 12px 5px 12px'
    },
    checkboxWrapper: {
        cursor: 'pointer',
        marginRight: 16
    },
    input: {
        color: theme.color.black,
        display: "block",
        width: "200%",
        padding: "10px 0 10px 50px",
        fontSize: '15px !important',
        fontFamily: "Open Sans",
        fontWeight: "600",
        border: "0",
        borderRadius: "3px",
        outline: 0,
        textIndent: "70px",
        transition: "all .3s ease-in-out",
        margin: "0px auto",
        alignItems: "center",
        justifyContent: "center",
        LeftMargin: "30px",
        alignSelf: "center"
    }

}));

const TAGS = {
    URGENT: {text: 'URGENT', backgroundColor: '#FEC400', color: '#FFFFFF'},
    NEW: {text: 'NEW', backgroundColor: '#29CC97', color: '#FFFFFF'},
    DEFAULT: {text: 'DEFAULT', backgroundColor: '#F0F1F7', color: '#9FA2B4'}
};

function Task(props) {
    const theme = useTheme();
    const classes = useStyles({theme});
    const [items, setItems] = useState([{title: '(예시) 오후 1시 커피- OOO 책임', checked: false}]);
    const [title, setTitle] = useState();
    const [contents, setContents] = useState();

    const {data} = useQuery(TaskQuery);

    useEffect(() => {
        if (data) {
            setContents(data.tasks);

        }
    }, [data]);


    const [create, {loading}] = useMutation(TaskCreateMutation, {
            refetchQueries: [{query: TaskQuery}],
            variables: {
                title: title
            },

            onError: () => {
                alert("주문 내용을 작성해주세요.")
            },
        }
    )

    console.log(contents &&
        contents.map((content) => (content._id)));
    console.log(contents);


    function onCheckboxClick(index) {
        setItems((prev) => {
            const newItems = [...prev];
            newItems[index].checked = newItems[index].checked ? false : true;
            return newItems;
        });
    }


    function renderAddButton() {
        return (
            <Row
                horizontal='center'
                vertical='center'
                className={[classes.addButton].join(' ')}
                onClick={create}
            >
                +
            </Row>
        );
    }

    return (
        <CardComponent
            containerStyles={props.containerStyles}
            title='📋 오늘의 주문 📋'
            subtitle='(예시) 12/30 오후 1시 커피- OOO 책임 연구원'

            items={[
                <Row horizontal='space-between' vertical='center'>
                    <span className={[classes.itemTitle, classes.greyTitle].join(' ')}>
                        <input type="text" placeholder="주문을 추가해주세요." onChange={e => setTitle(e.target.value)}
                               className={classes.input}/>
                    </span>
                    {renderAddButton()}
                </Row>,
                <Row>

                    {contents && contents.map((content) => (

                        <Row horizontal='space-between' vertical='center'>
                            <Row>
                                <table>
                                    <td><span className={classes.itemTitle}>{content.title}</span></td>
                                    <td><TaskDeleteButton post_id={content._id}/></td>
                                </table>
                            </Row>
                        </Row>


                    ))}
                </Row>


            ]}
        />
    );
}

function TaskComponent({classes, index, item = {}, onCheckboxClick, onTagClick}) {
    const {tag = {}} = item;
    return (
        <Row horizontal='space-between' vertical='center'>
            <Row>
                <div className={classes.checkboxWrapper} onClick={() => onCheckboxClick(index)}>
                    {item.checked ? <IconCheckboxOn/> : <IconCheckboxOff/>}
                </div>
                <span className={classes.itemTitle}>{item.title}</span>
            </Row>
            <TagComponent
                backgroundColor={tag.backgroundColor}
                classes={classes}
                color={tag.color}
                index={index}
                onClick={onTagClick}
                text={tag.text}
            />
        </Row>
    );
}

function TagComponent({backgroundColor, classes, color, index, onClick, text}) {
    return (
        <Row
            horizontal='center'
            vertical='center'
            style={{backgroundColor, color}}
            className={classes.tagStyles}
            onClick={() => onClick(index)}
        >
            {text}
        </Row>
    );
}

export default Task;
