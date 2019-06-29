.file "tests/codeGenOfMe/for_03.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/for_03.dcf:7:7:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/for_03.dcf:8:11:out of array bound error for a\n"
.LC2:
.string "%d "
.LC3:
.string "\n"
.LC4:
.string "tests/codeGenOfMe/for_03.dcf:13:7:out of array bound error for a\n"
.LC5:
.string "tests/codeGenOfMe/for_03.dcf:13:21:out of array bound error for a\n"
.LC6:
.string "tests/codeGenOfMe/for_03.dcf:13:31:out of array bound error for a\n"
.LC7:
.string "tests/codeGenOfMe/for_03.dcf:13:37:out of array bound error for a\n"
.LC8:
.string "tests/codeGenOfMe/for_03.dcf:13:42:out of array bound error for a\n"
.LC9:
.string "tests/codeGenOfMe/for_03.dcf:13:17:out of array bound error for a\n"
.LC10:
.string "tests/codeGenOfMe/for_03.dcf:14:22:out of array bound error for a\n"
.LC11:
.string "%d "
.LC12:
.string "tests/codeGenOfMe/for_03.dcf:13:57:out of array bound error for a\n"
.LC13:
.string "tests/codeGenOfMe/for_03.dcf:13:67:out of array bound error for a\n"
.LC14:
.string "tests/codeGenOfMe/for_03.dcf:13:73:out of array bound error for a\n"
.LC15:
.string "tests/codeGenOfMe/for_03.dcf:13:78:out of array bound error for a\n"
.LC16:
.string "tests/codeGenOfMe/for_03.dcf:13:53:out of array bound error for a\n"
.LC17:
.string "tests/codeGenOfMe/for_03.dcf:13:53:out of array bound error for a\n"
.LC18:
.string "\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $224, %rsp
movq $7,-216(%rbp)
movq $0,-152(%rbp)
.L1:
movq -152(%rbp),%rax
movq -216(%rbp),%r10
cmpq %r10,%rax
jge .L2
subq $16, %rsp
movq -152(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-224(%rbp)
movq -152(%rbp),%rax
cmpq $7,%rax
jl .L3
.L4:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L5
.L3:
movq -152(%rbp),%rax
cmpq $0,%rax
jge .L5
jmp .L4
.L5:
movq -152(%rbp),%rax
movq -224(%rbp),%r10
movq %r10,-208(%rbp,%rax,8)
movq -152(%rbp),%rax
cmpq $7,%rax
jl .L6
.L7:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L8
.L6:
movq -152(%rbp),%rax
cmpq $0,%rax
jge .L8
jmp .L7
.L8:
movq -152(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-232(%rbp)
movq -232(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -152(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-152(%rbp)
addq $16, %rsp
jmp .L1
.L2:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L9
.L10:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L11
.L9:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L11
jmp .L10
.L11:
movq $3,-208(%rbp)
.L12:
movq $2,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L13
.L14:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L15
.L13:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L15
jmp .L14
.L15:
movq -192(%rbp),%rax
movq %rax,-112(%rbp)
movq -112(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-120(%rbp)
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L16
.L17:
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L18
.L16:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L18
jmp .L17
.L18:
movq -208(%rbp),%rax
movq %rax,-104(%rbp)
movq -120(%rbp),%rax
movq -104(%rbp),%r10
addq %r10,%rax
movq %rax,-128(%rbp)
movq $1,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L19
.L20:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L21
.L19:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L21
jmp .L20
.L21:
movq -200(%rbp),%rax
movq %rax,-96(%rbp)
movq -128(%rbp),%rax
movq -96(%rbp),%r10
imulq %r10,%rax
movq %rax,-136(%rbp)
movq $4,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L22
.L23:
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L24
.L22:
movq $4,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L24
jmp .L23
.L24:
movq -176(%rbp),%rax
movq %rax,-88(%rbp)
movq -136(%rbp),%rax
movq -88(%rbp),%r10
cltd
idivq %r10
movq %rdx,-144(%rbp)
movq -144(%rbp),%rax
cmpq $7,%rax
jl .L25
.L26:
leaq .LC9(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L27:
movq -144(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-80(%rbp)
movq -80(%rbp),%rax
cmpq $8,%rax
jge .L28
subq $16, %rsp
movq $3,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L29
.L30:
leaq .LC10(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L31
.L29:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L31
jmp .L30
.L31:
movq -184(%rbp),%rax
movq %rax,-224(%rbp)
movq -224(%rbp),%rsi
leaq .LC11(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $2,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L32
.L33:
leaq .LC12(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L34
.L32:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L34
jmp .L33
.L34:
movq -192(%rbp),%rax
movq %rax,-40(%rbp)
movq -40(%rbp),%rax
movq $2,%r10
subq %r10,%rax
movq %rax,-48(%rbp)
movq $0,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L35
.L36:
leaq .LC13(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L37
.L35:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L37
jmp .L36
.L37:
movq -208(%rbp),%rax
movq %rax,-32(%rbp)
movq -48(%rbp),%rax
movq -32(%rbp),%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq $1,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L38
.L39:
leaq .LC14(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L40
.L38:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L40
jmp .L39
.L40:
movq -200(%rbp),%rax
movq %rax,-24(%rbp)
movq -56(%rbp),%rax
movq -24(%rbp),%r10
imulq %r10,%rax
movq %rax,-64(%rbp)
movq $4,%rax
movq $7,%r10
cmpq %r10,%rax
jl .L41
.L42:
leaq .LC15(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L43
.L41:
movq $4,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L43
jmp .L42
.L43:
movq -176(%rbp),%rax
movq %rax,-16(%rbp)
movq -64(%rbp),%rax
movq -16(%rbp),%r10
cltd
idivq %r10
movq %rdx,-72(%rbp)
movq -72(%rbp),%rax
cmpq $7,%rax
jl .L44
.L45:
leaq .LC16(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L46
.L44:
movq -72(%rbp),%rax
cmpq $0,%rax
jge .L46
jmp .L45
.L46:
movq -72(%rbp),%rax
movq -208(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -72(%rbp),%rax
cmpq $7,%rax
jl .L47
.L48:
leaq .LC17(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L49
.L47:
movq -72(%rbp),%rax
cmpq $0,%rax
jge .L49
jmp .L48
.L49:
movq -72(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-208(%rbp,%rax,8)
addq $16, %rsp
jmp .L12
.L28:
leaq .LC18(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret
.L25:
movq -144(%rbp),%rax
cmpq $0,%rax
jge .L27
jmp .L26
jmp .L27

