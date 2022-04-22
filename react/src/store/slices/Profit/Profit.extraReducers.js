import { getProfit } from './Profit.thunks';

const buildProfitExtraReducers = (builder) => {
    builder
        // GET PROFIT
        .addCase(getProfit.pending, (state) => {
            state.loading = true;
        })
        .addCase(getProfit.fulfilled, (state, action) => {
            state.loading = false;
            state.data = action.payload;
        })
        .addCase(getProfit.rejected, (state) => {
            state.loading = false;
        });
};

export default buildProfitExtraReducers;