import { Link, useLocation } from "react-router-dom"

const Navbar = () => {
  const location = useLocation()

  const isActive = (path: string) => {
    return location.pathname === path ? "text-white" : "text-gray-400 hover:text-white"
  }

  return (
    <nav className="bg-indigo-900/50 p-4">
      <div className="container mx-auto flex justify-center space-x-8">
        <Link 
          to="/" 
          className={`${isActive("/")} transition-colors duration-200`}
        >
          Home
        </Link>
        <Link 
          to="/portfolio" 
          className={`${isActive("/portfolio")} transition-colors duration-200`}
        >
          Portfolio
        </Link>
      </div>
    </nav>
  )
}

export default Navbar 