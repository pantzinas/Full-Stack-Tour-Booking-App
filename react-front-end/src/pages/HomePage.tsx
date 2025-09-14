import {useEffect} from "react";

const HomePage = () => {
    useEffect(() => {
        document.title = "Home Page";
    }, []);

    return (
        <>
            <div className="relative landscape-bg h-[500px]">
                <div className="absolute inset-0 bg-black/40"></div>
                <div className="relative z-10 flex items-center justify-center h-full">
                    <h1 className="text-white text-6xl md:text-6xl font-bold text-center font-DM-Sans">
                        Welcome to the Basque Country
                    </h1>
                </div>
            </div>
            <div className="bg-white mt-20 py-16 px-6">
                <h2 className="text-3xl font-bold text-center mb-12">
                    Our Most Popular Experiences
                </h2>
                <div className="mt-30 grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
                    <div className="text-center">
                        <img
                            src="../../public/hiking.png"
                            alt="Hiking Tour"
                            className="w-full h-48 object-cover rounded-lg shadow-md"
                        />
                        <p className="mt-4 text-lg font-medium">Hiking Tours</p>
                    </div>
                    <div className="text-center">
                        <img
                            src="../../public/gastro-tour.png"
                            alt="Food Tour"
                            className="w-full h-48 object-cover rounded-lg shadow-md"
                        />
                        <p className="mt-4 text-lg font-medium">Gastronomical Immersions</p>
                    </div>

                    {/* Card 3 */}
                    <div className="text-center">
                        <img
                            src="../../public/welness.png"
                            alt="Wellness Tour"
                            className="w-full h-48 object-cover rounded-lg shadow-md"
                        />
                        <p className="mt-4 text-lg font-medium">Wellness & Beauty Experiences</p>
                    </div>
                </div>
            </div>

        </>
    );
};

export default HomePage;