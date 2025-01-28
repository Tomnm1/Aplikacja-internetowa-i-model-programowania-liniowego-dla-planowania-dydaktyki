export const fetchWithAuth = async (
    url: string,
    options: RequestInit = {}
): Promise<Response> => {
    const token = localStorage.getItem('access_token');
    console.log('fetchWithAuth - URL:', url);
    console.log('fetchWithAuth - Token:', token);

    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    };

    if (token) {
        console.log(`Adding Authorization header: Bearer ${token.substring(0, 30)}...`);
    }

    const response = await fetch(url, {
        ...options,
        headers,
    });

    console.log(`fetchWithAuth - Response Status: ${response.status}`);

    if (response.status === 401) {
        localStorage.removeItem('access_token');
        localStorage.removeItem('auth');
        window.location.href = '/login';
    }

    return response;
};
