((fun (f) (f f)) (fun (fib) (fun (n) (if (< n 2) n (+ ((fib fib) (- n 1)) ((fib fib) (- n 2)))))))
