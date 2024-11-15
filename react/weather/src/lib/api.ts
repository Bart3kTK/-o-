import axios from "axios";

const url = process.env.REACT_APP_API_URL || "";

export async function mockApiFunction()
{
    return axios.get(url);
}