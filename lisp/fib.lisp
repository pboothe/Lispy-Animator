(((fun (*f) ((fun (f) (f f)) (fun (f) (*f (fun x ((f f) x)))))) (fun (f) (fun (n) (if (< n 2) n (+ (f (- n 1)) (f (- n 2))))))) 20)
