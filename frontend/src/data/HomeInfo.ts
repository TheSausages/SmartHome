export interface HomeInfo
{
    name: string,
    country: string,
    city: string,
    postCode: string,
    street: string,
    preferredTemp?: number,
    preferredHum?: number
}