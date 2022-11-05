import React from 'react';
import './App.css';
import NavBar from './components/NavBar/NavBar';
import Content from './components/Content/Content';
import Footer from './components/Footer/Footer';
import {BrowserRouter as Router} from 'react-router-dom';
import {styled} from '@mui/material';
import {QueryClient, QueryClientProvider} from 'react-query'

const queryClient = new QueryClient();

const StyledDiv = styled('div')({
  minHeight: "600px"
});

function App() {
  return (
    <>
    <QueryClientProvider client={queryClient}>
      <div className="App">
        <Router>
          <NavBar/>
          <StyledDiv>
          <div>
            <Content/>
          </div>
          </StyledDiv>
          <Footer/>
        </Router>
      </div>
    </QueryClientProvider>
    </>
  );
}

export default App;
