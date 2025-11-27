import React, { useState } from 'react';
import { FaChevronDown, FaChevronUp } from 'react-icons/fa';

export default function FAQ() {
  const [openItem, setOpenItem] = useState(0);

  const toggleItem = (index) => {
    setOpenItem(openItem === index ? null : index);
  };

  const faqItems = [
    {
      question: "What is LL-FARM?",
      answer: "LL-FARM is an agriculture marketplace platform that connects farmers directly with buyers, enabling the sale of fresh farm produce without middlemen."
    },
    {
      question: "How can I become a farmer on LL-FARM?",
      answer: "You can register as a farmer by clicking 'Become a Farmer' and filling out the registration form. After admin approval, you can start listing your products."
    },
    {
      question: "Are the products fresh and directly from farms?",
      answer: "Yes! All products are listed directly by verified farmers, ensuring freshness and quality. Products are delivered straight from the farm to your doorstep."
    },
    {
      question: "What payment methods are accepted?",
      answer: "We accept secure online payments through Razorpay, including credit/debit cards, UPI, net banking, and digital wallets."
    },
    {
      question: "How do farmers benefit from LL-FARM?",
      answer: "Farmers receive fair prices by selling directly to consumers, eliminating middlemen. They also get access to a wider customer base and analytics to track their sales."
    },
    {
      question: "Is my personal information secure on LL-FARM?",
      answer: "Absolutely! We use advanced encryption and secure payment gateways to protect your personal and financial information."
    },
    {
      question: "Can I track my orders?",
      answer: "Yes, you can track all your orders through the 'My Orders' section in your buyer dashboard."
    },
    {
      question: "What categories of products are available?",
      answer: "We offer various categories including Vegetables, Fruits, Grains, Dairy products, Spices, and more - all fresh from local farms."
    }
  ];

  return (
    <div className="max-w-4xl mx-auto py-12 px-4">
      <h1 className="text-3xl font-bold text-center mb-10">Frequently Asked Questions</h1>
      
      <div className="space-y-4">
        {faqItems.map((item, index) => (
          <div 
            key={index} 
            className="bg-gray-50 border border-gray-200 rounded-md overflow-hidden"
          >
            <button
              onClick={() => toggleItem(index)}
              className="w-full text-left py-4 px-6 flex justify-between items-center focus:outline-none hover:bg-gray-100"
            >
              <span className="font-medium">{item.question}</span>
              {openItem === index ? (
                <FaChevronUp className="text-gray-600" />
              ) : (
                <FaChevronDown className="text-gray-600" />
              )}
            </button>
            
            {openItem === index && (
              <div className="px-6 py-4 bg-white">
                <p className="text-gray-600">{item.answer}</p>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}