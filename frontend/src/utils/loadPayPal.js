let paypalPromise = null;

export function loadPayPal(clientId) {
  if (window.paypal) {
    return Promise.resolve(window.paypal);
  }

  if (!paypalPromise) {
    paypalPromise = new Promise((resolve, reject) => {
      const script = document.createElement("script");
      script.src = `https://www.sandbox.paypal.com/sdk/js?client-id=${clientId}&currency=USD`;
      script.async = true;
      script.onload = () => resolve(window.paypal);
      script.onerror = reject;
      document.body.appendChild(script);
    });
  }

  return paypalPromise;
}
