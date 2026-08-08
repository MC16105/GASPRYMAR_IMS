import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import MainLayout from "./layouts/MainLayout";


function App() {
  return (
      <BrowserRouter>
        <Routes>

          {/* Login */}
          <Route path="/" element={<Login />} />

          {/* Dashboard y páginas internas */}
          <Route path="/dashboard" element={<MainLayout />}>

            {/* /dashboard */}
            <Route index element={<Dashboard />} />



          </Route>

        </Routes>
      </BrowserRouter>
  );
}

export default App;export default App;