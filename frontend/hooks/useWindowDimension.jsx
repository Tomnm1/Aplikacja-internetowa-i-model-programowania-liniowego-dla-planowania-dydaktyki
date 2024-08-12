import { useState, useEffect } from 'react';

export function useWindowDimension() {
    const [dimension, setDimension] = useState([
        window.innerWidth,
        window.innerHeight,
    ]);
    useEffect(() => {
        const debouncedResizeHandler = debounce(() => {
            console.log('***** debounced resize'); // See the cool difference in console
            setDimension([window.innerWidth, window.innerHeight]);
        }, 100); // 100ms
        window.addEventListener('resize', debouncedResizeHandler);
        return () => window.removeEventListener('resize', debouncedResizeHandler);
    }, []);
    return dimension;
}

function debounce(fn, ms) {
    let timer;
    return _ => {
        clearTimeout(timer);
        timer = setTimeout(_ => {
            timer = null;
            fn.apply(this, arguments);
        }, ms);
    };
}
