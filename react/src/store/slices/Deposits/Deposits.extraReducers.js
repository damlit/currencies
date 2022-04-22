import { getDeposits } from './Deposits.thunks';

const buildDepositsExtraReducers = (builder) => {
    builder
        // GET DEPOSITS
        .addCase(getDeposits.pending, (state) => {
            state.loading = true;
        })
        .addCase(getDeposits.fulfilled, (state, action) => {
            state.loading = false;
            state.depositsByPage = action.payload;
        })
        .addCase(getDeposits.rejected, (state) => {
            state.loading = false;
        });
};

export default buildDepositsExtraReducers;