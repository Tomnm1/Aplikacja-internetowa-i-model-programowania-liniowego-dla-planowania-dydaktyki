import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './components/App.tsx'
import './index.css'
import {Provider} from "react-redux";
import {store} from "./app/store.ts";
import { SnackbarProvider } from 'notistack';
import {StyledEngineProvider} from "@mui/material";


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
      <Provider store={store}>
          <StyledEngineProvider injectFirst>
              <SnackbarProvider
                  maxSnack={3}
                  anchorOrigin={{
                      vertical: 'bottom',
                      horizontal: 'left',
                  }}
                  autoHideDuration={6000}
              >
                <App />
              </SnackbarProvider>
          </StyledEngineProvider>
      </Provider>
  </React.StrictMode>,
)
