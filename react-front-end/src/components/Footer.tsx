const Footer = () => {
    const currentYear: number = new Date().getFullYear();

    return (
        <>
            <footer className="bg-app-gray text-black mt-30">
                <div className="flex items-center justify-between py-10 px-6">
                    <div className="flex items-center">
                        <img src="../../public/invato-imagotype-transpoarent%201.png" className="w-30 h-auto" alt="Logo" />
                    </div>
                    <div className="flex-1 text-center">
                        <ul className="space-y-2">
                            <li>Hiking Tours</li>
                            <li>Food Tours</li>
                            <li>Wellness Tours</li>
                            <li>&copy; {currentYear} Your Guided Tour Experience. All rights reserved.</li>
                        </ul>
                    </div>
                    <div className="w-[120px]"></div>
                </div>
            </footer>
        </>
    )
};

export default Footer;