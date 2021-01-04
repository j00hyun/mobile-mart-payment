const color = {
    brightBlue: '#3498db',
    darkGrayishBlue: '#8b8d94',
    darkRed: '#624444',
    grayishBlue: '#A4A6B3',
    grayishBlue2: '#9fa2b4',
    grayishBlue3: '#bdc3c7',
    lightBlue: '#3751FF',
    lightGrayishBlue: '#f7f8fc',
    lightGrayishBlue2: '#DFE0EB',
    paleBlue: '#DDE2FF',
    paleBlueTransparent: 'rgba(221, 226, 255, 0.08)',
    veryDarkGrayishBlue: '#373a47',
    darkgreen: "#063014",
    lightyellow: "#d2cdc1",
    yellow: "#f1bf4b",
    black: "#0a0a0a",
};

const typography = {
    cardTitle: {
        fontSize: 19,
        lineHeight: '24px',
        letterSpacing: '0.4px'
    },
    smallSubtitle: {
        fontSize: 12,
        lineHeight: '16px',
        letterSpacing: '0.1px'
    },
    link: {
        fontWeight: '600',
        fontSize: 14,
        lineHeight: '20px',
        letterSpacing: '0.2px',
        color: color.lightBlue,
        textAlign: 'right',
        cursor: 'pointer',
        textDecoration: 'underline',
        '&:hover': {
            color: color.grayishBlue
        }
    },
    itemTitle: {
        fontWeight: 600,
        fontSize: 14,
        lineHeight: '20px',
        letterSpacing: 0.2,
        align:"center",
        textAlign: "center"
    },
    title: {
        fontWeight: 'bold',
        fontSize: 24,
        lineHeight: '30px',
        letterSpacing: 0.3,
        textAlign:"center"
    }
};

export default {
    color,
    typography
};
