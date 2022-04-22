import { getQuotes } from './Quotes.thunks';

const buildUserExtraReducers = (builder) => {
    builder
        // GET LAST QUOTES
        .addCase(getQuotes.pending, (state) => {
            state.loading = true;
        })
        .addCase(getQuotes.fulfilled, (state, action) => {
            state.loading = false;
            state.data = action.payload;
        })
        .addCase(getQuotes.rejected, (state) => {
            state.loading = false;
        });
};

export default buildUserExtraReducers;