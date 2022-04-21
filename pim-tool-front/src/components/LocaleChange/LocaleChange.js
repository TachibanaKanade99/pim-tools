import React, { useState } from 'react';
import counterpart from 'counterpart';

import './LocaleChange.css';

export default function LocaleChange(props) {
    const [enLocaleColor, setEnLocaleColor] = useState('#2f85fa');
    const [frLocaleColor, setFrLocaleColor] = useState('#666666');

    function handleChange(e) {
        e.preventDefault();
        let localeVal = e.target.getAttribute("value");
        counterpart.setLocale(localeVal);
        
        if (localeVal === 'en') {
            setEnLocaleColor('#2f85fa');
            setFrLocaleColor('#666666');
        }
        else {
            setEnLocaleColor('#666666');
            setFrLocaleColor('#2f85fa');
        }
    }

    return (
        <div style={{ marginTop: '25px' }}>
            <a className="locale-select" style={{ color: enLocaleColor }} href="/" value="en" onClick={handleChange}>EN</a>
            <span style={{ color: '#2f85fa' }}>|</span>
            <a className="locale-select" style={{ color: frLocaleColor }} href="/" value="fr" onClick={handleChange}>FR</a>
        </div>
    )
}