.file "tests/codeGenOfMe/print_arr.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/print_arr.dcf:6:7:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/print_arr.dcf:7:22:out of array bound error for a\n"
.LC2:
.string "%d\n"
.LC3:
.string "tests/codeGenOfMe/print_arr.dcf:13:31:out of array bound error for a\n"
.LC4:
.string "tests/codeGenOfMe/print_arr.dcf:13:20:out of array bound error for a\n"
.LC5:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $176, %rsp
movq $10,-168(%rbp)
movq $0,-80(%rbp)
.L1:
movq -80(%rbp),%rax
cmpq $10,%rax
jge .L2
subq $16, %rsp
movq -80(%rbp),%rax
cmpq $10,%rax
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
movq -80(%rbp),%rax
cmpq $0,%rax
jge .L5
jmp .L4
.L5:
movq -80(%rbp),%rax
movq -80(%rbp),%r10
movq %r10,-160(%rbp,%rax,8)
movq -80(%rbp),%rax
cmpq $10,%rax
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
movq -80(%rbp),%rax
cmpq $0,%rax
jge .L8
jmp .L7
.L8:
movq -80(%rbp),%rax
movq -160(%rbp,%rax,8),%r10
movq %r10,-176(%rbp)
movq -176(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -80(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-80(%rbp)
addq $16, %rsp
jmp .L1
.L2:
movq $10,-72(%rbp)
movq $8,-64(%rbp)
movq $2,-56(%rbp)
movq -72(%rbp),%rax
movq -64(%rbp),%r10
subq %r10,%rax
movq %rax,-32(%rbp)
movq -56(%rbp),%rax
movq $1,%r10
imulq %r10,%rax
movq %rax,-24(%rbp)
movq -32(%rbp),%rax
movq -24(%rbp),%r10
addq %r10,%rax
movq %rax,-40(%rbp)
movq $5,%rax
movq $10,%r10
cmpq %r10,%rax
jl .L9
.L10:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L11
.L9:
movq $5,%rax
movq $0,%r10
cmpq %r10,%rax
jge .L11
jmp .L10
.L11:
movq -120(%rbp),%rax
movq %rax,-16(%rbp)
movq -40(%rbp),%rax
movq -16(%rbp),%r10
subq %r10,%rax
movq %rax,-48(%rbp)
movq -48(%rbp),%rax
cmpq $10,%rax
jl .L12
.L13:
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L14
.L12:
movq -48(%rbp),%rax
cmpq $0,%rax
jge .L14
jmp .L13
.L14:
movq -48(%rbp),%rax
movq -160(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rsi
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

