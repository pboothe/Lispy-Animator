(((fun (f) (f f) ) 
  (fun (f) 
    (fun (n) 
     (if (< n 2) 1
      (* n ((f f) (- n 1))))))) 
6)
