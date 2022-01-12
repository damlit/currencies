export class Deposit {
    constructor(soldCurrency, boughtCurrency, quote, soldSum) {
        this.soldCurrency = soldCurrency;
        this.boughtCurrency = boughtCurrency;
        this.quote = quote;
        this.soldSum = soldSum;
    }
}