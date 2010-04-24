from random import choice, randint
def rb(): return choice([True, False])

def rand(depth=6, forced=True, t=True):
    if depth == 0:
        if forced:
            if t:
                return "1"
            else:
                return "0"
        else:
            if rb():
                return "1"
            else:
                return "0"
    else:
        c = choice(["not", "and", "or"])
        if "not" == c:
            return "(not " + rand(depth-1, forced, not t) + ")"
        elif "and" == c:
            if not forced:
                return "(and " + rand(depth-1, False, rb()) + " " + rand(depth-1, False, rb()) + ")"
            elif t:
                return "(and " + rand(depth-1, forced, True) + " " + rand(depth-1, forced, True) + ")"
            else:
                if rb():
                    return "(and " + rand(depth-1, False, rb()) + " " + rand(depth-1, True, False) + ")"
                else:
                    return "(and " + rand(depth-1, True, False) + " " + rand(depth-1, False, rb()) + ")"
        elif "or" == c:
            if not forced:
                return "(or " + rand(depth-1, forced, rb()) + " " + rand(depth-1, forced, rb()) + ")"
            elif not t:
                return "(or " + rand(depth-1, forced, False) + " " + rand(depth-1, forced, False) + ")"
            else:
                if rb():
                    return "(or " + rand(depth-1, False, rb()) + " " + rand(depth-1, True, True) + ")"
                else:
                    return "(or " + rand(depth-1, True, True) + " " + rand(depth-1, False, rb()) + ")"
        
            
    
    

if __name__ == '__main__':
    print rand()
