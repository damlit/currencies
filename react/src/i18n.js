import i18next from "i18next";
import common_en from './translations/en/common.json';
import common_pl from './translations/pl/common.json';

i18next.init({
    interpolation: { escapeValue: false },
    react: {
        useSuspense: false,
        wait: true,
    },
    fallbackLng: ['en', 'pl'],
    resources: {
        en: { common: common_en },
        pl: { common: common_pl }
    }
});

export default i18next;