.file "tests/codeGenOfMe/BoundCheck_01.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/BoundCheck_01.dcf:6:5:out of array bound error for a\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $96, %rsp
movq $9,-88(%rbp)
movq $9,-8(%rbp)
movq -8(%rbp),%rax
cmpq $9,%rax
jl .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L1
.L1:
movq -8(%rbp),%rax
movq $90,-80(%rbp,%rax,8)
leave
ret

