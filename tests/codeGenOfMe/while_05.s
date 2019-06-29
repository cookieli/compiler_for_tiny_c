.file "tests/codeGenOfMe/while_05.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/while_05.dcf:5:4:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/while_05.dcf:6:4:out of array bound error for a\n"
.LC2:
.string "tests/codeGenOfMe/while_05.dcf:7:4:out of array bound error for a\n"
.LC3:
.string "tests/codeGenOfMe/while_05.dcf:13:27:out of array bound error for a\n"
.LC4:
.string "tests/codeGenOfMe/while_05.dcf:13:10:out of array bound error for a\n"
.LC5:
.string "tests/codeGenOfMe/while_05.dcf:13:43:out of array bound error for a\n"
.LC6:
.string "tests/codeGenOfMe/while_05.dcf:13:51:out of array bound error for a\n"
.LC7:
.string "tests/codeGenOfMe/while_05.dcf:13:63:out of array bound error for a\n"
.LC8:
.string "tests/codeGenOfMe/while_05.dcf:13:76:out of array bound error for a\n"
.LC9:
.string "tests/codeGenOfMe/while_05.dcf:13:83:out of array bound error for a\n"
.LC10:
.string "tests/codeGenOfMe/while_05.dcf:14:35:out of array bound error for a\n"
.LC11:
.string "hello world %d\n"
.LC12:
.string "tests/codeGenOfMe/while_05.dcf:15:7:out of array bound error for a\n"
.LC13:
.string "tests/codeGenOfMe/while_05.dcf:15:7:out of array bound error for a\n"
.LC14:
.string "tests/codeGenOfMe/while_05.dcf:16:14:out of array bound error for a\n"
.LC15:
.string "tests/codeGenOfMe/while_05.dcf:16:7:out of array bound error for a\n"
.LC16:
.string "tests/codeGenOfMe/while_05.dcf:18:19:out of array bound error for a\n"
.LC17:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $192, %rsp
movq $4,-192(%rbp)
movq $0,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L3
.L1:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L3
jmp .L2
.L3:
movq $1,-184(%rbp)
movq $1,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L4
.L5:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L6
.L4:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L6
jmp .L5
.L6:
movq $6,-176(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L7
.L8:
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L9
.L7:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L9
jmp .L8
.L9:
movq $3,-168(%rbp)
movq $5,-152(%rbp)
movq $6,-144(%rbp)
movq $8,-136(%rbp)
movq $2,-128(%rbp)
.L10:
movq -152(%rbp),%rax
movq -144(%rbp),%r10
addq %r10,%rax
movq %rax,-104(%rbp)
movq -136(%rbp),%rax
movq -128(%rbp),%r10
addq %r10,%rax
movq %rax,-96(%rbp)
movq -104(%rbp),%rax
movq -96(%rbp),%r10
subq %r10,%rax
movq %rax,-112(%rbp)
movq $0,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L11
.L12:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L13
.L11:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L13
jmp .L12
.L13:
movq -184(%rbp),%rax
movq %rax,-80(%rbp)
movq -80(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-88(%rbp)
movq -112(%rbp),%rax
movq -88(%rbp),%r10
imulq %r10,%rax
movq %rax,-120(%rbp)
movq -120(%rbp),%rax
cmpq $4,%rax
jl .L14
.L15:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L16
.L14:
movq -120(%rbp),%rax
cmpq $0,%rax
jge .L16
jmp .L15
.L16:
movq -120(%rbp),%rax
movq -184(%rbp,%rax,8),%r10
movq %r10,-72(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L17
.L18:
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L19
.L17:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L19
jmp .L18
.L19:
movq -168(%rbp),%rax
movq %rax,-64(%rbp)
movq $0,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L20
.L21:
leaq .LC6(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L22
.L20:
movq $0,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L22
jmp .L21
.L22:
movq -184(%rbp),%rax
movq %rax,-48(%rbp)
movq -48(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-56(%rbp)
movq $3,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L23
.L24:
leaq .LC7(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L25
.L23:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L25
jmp .L24
.L25:
movq -160(%rbp),%rax
movq %rax,-40(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L26
.L27:
leaq .LC8(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L28
.L26:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L28
jmp .L27
.L28:
movq -168(%rbp),%rax
movq %rax,-32(%rbp)
movq $1,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L29
.L30:
leaq .LC9(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L31:
movq -176(%rbp),%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
movq $3,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
movq -72(%rbp),%rax
cmpq $6,%rax
jge .L32
movq -64(%rbp),%rax
movq -56(%rbp),%r10
cmpq %r10,%rax
jl .L32
.L33:
subq $32, %rsp
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L34
.L35:
leaq .LC10(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L36
.L34:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L36
jmp .L35
.L36:
movq -168(%rbp),%rax
movq %rax,-224(%rbp)
movq -224(%rbp),%rsi
leaq .LC11(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L37
.L38:
leaq .LC12(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L39
.L37:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L39
jmp .L38
.L39:
movq -168(%rbp),%rax
movq %rax,-216(%rbp)
movq -216(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-216(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L40
.L41:
leaq .LC13(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L42
.L40:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L42
jmp .L41
.L42:
movq -216(%rbp),%rax
movq %rax,-168(%rbp)
movq $2,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L43
.L44:
leaq .LC14(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L45
.L43:
movq $2,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L45
jmp .L44
.L45:
movq -168(%rbp),%rax
movq %rax,-200(%rbp)
movq -200(%rbp),%rax
movq $4,%r10
addq %r10,%rax
movq %rax,-208(%rbp)
movq $3,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L46
.L47:
leaq .LC15(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L48
.L46:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L48
jmp .L47
.L48:
movq -208(%rbp),%rax
movq %rax,-160(%rbp)
addq $32, %rsp
jmp .L10
.L32:
movq -40(%rbp),%rax
cmpq $20,%rax
jge .L49
jmp .L33
.L49:
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jge .L50
jmp .L33
.L50:
movq $3,%rax
movq $4,%r10
cmpq %r10,%rax
jl .L51
.L52:
leaq .LC16(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L53
.L51:
movq $3,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L53
jmp .L52
.L53:
movq -160(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rsi
leaq .LC17(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret
jmp .L32
.L29:
movq $1,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L31
jmp .L30
jmp .L31

