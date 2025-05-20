import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div className="relative">
      <div className="mx-auto max-w-7xl">
        <div className="relative z-10 pt-14 lg:w-full lg:max-w-2xl">
          <div className="relative px-6 py-32 sm:py-40 lg:px-8 lg:py-56 lg:pr-0">
            <div className="mx-auto max-w-2xl lg:mx-0 lg:max-w-xl">
              <h1 className="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl">
                Find Your Dream Home
              </h1>
              <p className="mt-6 text-lg leading-8 text-gray-600">
                Discover a place you'll love to live. Browse through our curated selection of premium properties and find your perfect match.
              </p>
              <div className="mt-10 flex items-center gap-x-6">
                <Link
                  to="/properties"
                  className="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                  Browse Properties
                </Link>
                <Link to="/contact" className="text-sm font-semibold leading-6 text-gray-900">
                  Contact Us <span aria-hidden="true">→</span>
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="bg-gray-50 lg:absolute lg:inset-y-0 lg:right-0 lg:w-1/2">
        <img
          className="aspect-[3/2] object-cover lg:aspect-auto lg:h-full lg:w-full"
          src="https://images.pexels.com/photos/1396122/pexels-photo-1396122.jpeg"
          alt="Modern home interior"
        />
      </div>
    </div>
  );
}