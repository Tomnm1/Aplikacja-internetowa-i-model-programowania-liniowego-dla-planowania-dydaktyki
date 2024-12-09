export const fetchWithAuth = async (
    url: string,
    options: RequestInit = {}
): Promise<Response> => {
    const token = localStorage.getItem('access_token');
    const headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    };
    const response = await fetch(url, {
        ...options,
        headers,
    });
    if (response.status === 401) {
        localStorage.removeItem('access_token');
        localStorage.removeItem('auth');
        window.location.href = '/login';
    }
    return response;
};
