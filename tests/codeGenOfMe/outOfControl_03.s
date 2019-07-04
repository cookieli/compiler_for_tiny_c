.file "tests/codeGenOfMe/outOfControl_03.dcf"
.text
.section .rodata
.LC0:
.string "%d\n"
.text
.globl func
.type func, @function
func:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,-8(%rbp)
.L5:
movq -8(%rbp),%rax
cmpq $0,%rax
jle .L6
movq -8(%rbp),%rax
cmpq $3,%rax
jne .L7
movq $0,%rax
jmp .L3
jmp .L7
.L7:
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
jmp .L5
.L6:
movq $0,%rax
jmp .L3
.L3:
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,%rax
call func
movq %rax,-8(%rbp)
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L4:
leave
ret

