from random import choice, randint
def rb(): return choice([True, False])

def rand(depth=6, t=True):
    if depth == 0:
        if t:
            return "1"
        else:
            return "0"
    else:
        c = choice(["not", "and", "or"])
        if "not" == c:
            return "(not " + rand(depth-1, not t) + ")"
        elif "and" == c:
            if t:
                return "(and " + rand(depth-1, True) + " " + rand(depth-1, True) + ")"
            else:
                if rb():
                    return "(and " + rand(depth-1, rb()) + " " + rand(depth-1, False) + ")"
                else:
                    return "(and " + rand(depth-1, False) + " " + rand(depth-1, rb()) + ")"
        elif "or" == c:
            if not t:
                return "(or " + rand(depth-1, False) + " " + rand(depth-1, False) + ")"
            else:
                if rb():
                    return "(or " + rand(depth-1, rb()) + " " + rand(depth-1, True) + ")"
                else:
                    return "(or " + rand(depth-1, True) + " " + rand(depth-1, rb()) + ")"
        
if __name__ == '__main__':
    print rand()
